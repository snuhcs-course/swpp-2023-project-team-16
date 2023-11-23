from flask import Flask, jsonify
from flask_socketio import SocketIO
from apscheduler.schedulers.background import BackgroundScheduler
import requests
from ultralytics import YOLO
import glob

app = Flask(__name__)
socketio = SocketIO(app)
model = YOLO('/nfs/home/sunwoo1/line_detection/ultralytics/runs/detect/train9/weights/best.pt')
image_directory = '/nfs/home/sunwoo1/line_detection/frames/3'
image_paths = glob.glob(f'{image_directory}/*.jpg')

# Initialize the scheduler
scheduler = BackgroundScheduler(daemon=True)
scheduler.start()

def perform_object_detection():
    with app.app_context():
        results_list = []
        for image_path in image_paths:
            results = model(image_path)

            # Extracting relevant information from each Results object
            for result in results:
                result_info = {
                    "boxes": {
                        "xyxy": result.boxes.xyxy.tolist() if result.boxes.xyxy is not None else None,
                        "conf": result.boxes.conf.tolist() if result.boxes.conf is not None else None,
                        "cls": result.boxes.cls.tolist() if result.boxes.cls is not None else None,
                        "id": result.boxes.id.tolist() if result.boxes.id is not None else None,
                        "xywh": result.boxes.xywh.tolist() if result.boxes.xywh is not None else None,
                        "xyxyn": result.boxes.xyxyn.tolist() if result.boxes.xyxyn is not None else None,
                        "xywhn": result.boxes.xywhn.tolist() if result.boxes.xywhn is not None else None,
                    },
                    # Add other attributes as needed
                }
                results_list.append(result_info)

        # Emit the results to the client
        socketio.emit('object_detection_results', jsonify(results_list))

        # Send the results to another server using a PUT request
        # Replace 'http://other_server_endpoint' with the actual endpoint of the other server
        requests.put('http://192.168.0.4:5000/receive_results', json=results_list)

# Schedule the object detection task to run every 5 seconds
scheduler.add_job(perform_object_detection, 'interval', seconds=5)

if __name__ == '__main__':
    socketio.run(app, host='192.168.0.4', port=7777)
