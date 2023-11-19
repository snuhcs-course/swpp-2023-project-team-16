from PIL import Image
from ultralytics import YOLO
import glob

# Load a pretrained YOLOv8n model
model = YOLO('/nfs/home/sunwoo1/line_detection/ultralytics/runs/detect/train5/weights/best.pt')

# Define the directory containing the images
image_directory = '/nfs/home/sunwoo1/line_detection/frames/3'

# Get a list of image file paths in the directory
image_paths = glob.glob(f'{image_directory}/*.jpg')  # Change the file extension if your images have a different format

# # Process each image and save the results
# for image_path in image_paths:
#     results = model(image_path)  # Inference on each image

#     for r in results:
#         im_array = r.plot()  # Plot a BGR numpy array of predictions
#         im = Image.fromarray(im_array[..., ::-1])  # RGB PIL image

#         # Show the image
#         # im.show()

#         # Save the image with a unique name based on the original image file name
#         output_filename = "/nfs/home/sunwoo1/line_detection/inference_result_yolo2/" + f"results_{image_path.split('/')[-1]}"
#         im.save(output_filename)  # Save image with a unique name
#         print(r.boxes)

#
#
#           bounding box inference
# 
#
from ultralytics import YOLO

# Load a pretrained YOLOv8n model
model = YOLO('/nfs/home/sunwoo1/runs/detect/train/weights/best.pt')

# Run inference on an image
results = model('/nfs/home/sunwoo1/line_detection/frames/3/frame_625000.jpg')  # results list

# View results
for r in results:
    print(r.boxes)  # print the Boxes object containing the detection bounding boxes





#
#
#           사진 1장에 대한 inference
# 
#
# from ultralytics import YOLO

# # Load a pretrained YOLOv8n model
# model = YOLO('/nfs/home/sunwoo1/line_detection/ultralytics/runs/detect/train3/weights/best.pt')

# # Run inference on 'bus.jpg' with arguments
# model.predict('/nfs/home/sunwoo1/line_detection/frames/3/frame_7000.jpg', save=True, imgsz=640, conf=0.5)

