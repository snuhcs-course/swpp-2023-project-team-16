import os
import time
import glob
import requests, json
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler
from ultralytics import YOLO
import numpy as np

model = YOLO('/nfs/home/sunwoo1/line_detection/ultralytics/runs/detect/train9/weights/best.pt')
image_directory = "/nfs/home/sunwoo1/line_detection/ultralytics/cctv_frame_socket"
image_paths = glob.glob(f'{image_directory}/*.jpg')
results_list = []


class OnMyWatch:
    # Set the directory on watch
    
    image_directory = "/nfs/home/sunwoo1/line_detection/ultralytics/cctv_frame_socket"

    def __init__(self):
        self.observer = Observer()

    def run(self):
        event_handler = Handler()
        self.observer.schedule(event_handler, self.image_directory, recursive=True)
        self.observer.start()
        try:
            while True:
                time.sleep(5)
        except:
            self.observer.stop()
            print("Observer Stopped")

        self.observer.join()


class Handler(FileSystemEventHandler):

    @staticmethod
    def on_any_event(event):
        waiting_people = 0

        if event.is_directory:
            return None

        # elif event.event_type == 'created':
        #     # Event is created, you can process it now
        #     print("created")
        elif event.event_type == 'modified' or event.event_type == 'created':
            print("발생:", event.event_type)
            # inference part
            image_path = event.src_path
            print("event_src_path:", image_path)


            # 이미지 존재하지 않을 때, 에러 handle
            try:
                results = model(image_path)
            except FileNotFoundError as e:
                print(f"Error processing image: {e}")
                return 
            
            results = model(image_path)

            # Find the result with the highest confidence score
            best_result = max(results, key=lambda r: r.boxes.conf.max() if r.boxes.conf is not None else 0, default=None)

            if best_result is not None and best_result.boxes.xyxy is not None:
                # Extracting x_min, y_min, x_max, y_max from the best result
                x_min, y_min, x_max, y_max = best_result.boxes.xyxy[0].tolist()
                confidence_score = best_result.boxes.conf.max().item()

                bounding_box_length = np.sqrt((x_max-x_min)**2 + (y_max-y_min)**2)
                
                # bbox 대각선 길이로 사람수 유추 (1.1~1.2배하면 될 듯)
                # waiting_people = 5.9431*np.exp(0.0049*bounding_box_length)
                waiting_people = 1.1*(0.0001*bounding_box_length**2 + 0.0856*bounding_box_length - 12.8080) #1.2배 임의로 추가함
                
                
                print("bounding box:", waiting_people)
                results_list.append(waiting_people)
                print("results_list:", results_list)
                # result_info = {
                # "waiting_peope": waiting_people,
                # }
                # results_list.append(result_info)
                
               
            if len(results_list) == 1:
                middle_result = results_list[len(results_list) // 2]
            
                print("기다리는 사람 수:", middle_result)
                data = {
                    # 'waiting_people': 52,
                    'waiting_people' : int(middle_result),
                }
                response = requests.put("http://54.180.118.50:8000/dropoff/update-waiting", data=json.dumps(data))
                print(response.status_code)

                # # Remove processed image files
                # for image_path in image_paths:
                #     os.remove(image_path)
                results_list.clear()

        elif event.event_type == 'deleted':
            print("deleted")

        


if __name__ == '__main__':
    watch = OnMyWatch()
    watch.run()
