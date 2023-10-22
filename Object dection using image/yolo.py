# USAGE
# python yolo.py --image images/baggage_claim.jpg
# Import the necessary packages
# Copyright from GitHub YOLOv5

import numpy as np
import argparse
import time
import cv2
import os

# Construct the argument parser and parse the arguments
arg_parser = argparse.ArgumentParser()
arg_parser.add_argument("-i", "--image", required=True,
                help="path to input image")
arg_parser.add_argument("-c", "--confidence", type=float, default=0.5,
                help="minimum probability to filter weak detections")
arg_parser.add_argument("-t", "--threshold", type=float, default=0.3,
                help="threshold when applying non-maxima suppression")
args = vars(arg_parser.parse_args())

# Load the COCO class labels our YOLO model was trained on
# labels_path = 'yolo-coco/coco.names'
labels_path = '../yolo-coco/coco.names'
LABELS = open(labels_path).read().strip().split("\n")

# Initialize a list of colors to represent each possible class label
np.random.seed(42)
COLORS = np.random.randint(0, 255, size=(len(LABELS), 3),
                           dtype="uint8")

# Derive the paths to the YOLO weights and model configuration
weights_path = 'yolo-coco/yolov3.weights'
config_path = 'yolo-coco/yolov3.cfg'

# Load our YOLO object detector trained on the COCO dataset (80 classes)
print("[INFO] Loading YOLO from disk...")
net = cv2.dnn.readNetFromDarknet(config_path, weights_path)

# Load our input image and grab its spatial dimensions
image = cv2.imread(args["image"])
(H, W) = image.shape[:2]

# Determine only the *output* layer names that we need from YOLO
layer_names = net.getLayerNames()
output_layer_names = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]

# Construct a blob from the input image and then perform a forward
# pass of the YOLO object detector, giving us our bounding boxes and
# associated probabilities
blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (416, 416),
                            swapRB=True, crop=False)
net.setInput(blob)
start_time = time.time()
layer_outputs = net.forward(output_layer_names)
# [, frame, no_of_detections, [class_id, class_score, conf, x, y, h, w]
end_time = time.time()

# Show timing information on YOLO
print("[INFO] YOLO took {:.6f} seconds".format(end_time - start_time))

# Initialize our lists of detected bounding boxes, confidences, and
# class IDs, respectively
bounding_boxes = []
confidences = []
class_ids = []

# Loop over each of the layer outputs
for output in layer_outputs:
    # Loop over each of the detections
    for detection in output:
        # Extract the class ID and confidence (i.e., probability) of
        # the current object detection
        scores = detection[5:]
        class_id = np.argmax(scores)
        confidence = scores[class_id]

        # Filter out weak predictions by ensuring the detected
        # probability is greater than the minimum probability
        if confidence > args["confidence"]:
            # Scale the bounding box coordinates back relative to the
            # size of the image, keeping in mind that YOLO actually
            # returns the center (x, y)-coordinates of the bounding
            # box followed by the box's width and height
            box = detection[0:4] * np.array([W, H, W, H])
            (center_x, center_y, width, height) = box.astype("int")

            # Use the center (x, y)-coordinates to derive the top and
            # left corner of the bounding box
            x = int(center_x - (width / 2))
            y = int(center_y - (height / 2))

            # Update our list of bounding box coordinates, confidences,
            # and class IDs
            bounding_boxes.append([x, y, int(width), int(height)])
            confidences.append(float(confidence))
            class_ids.append(class_id)

# Apply non-maxima suppression to suppress weak, overlapping bounding
# boxes
indexes = cv2.dnn.NMSBoxes(bounding_boxes, confidences, args["confidence"],
                       args["threshold"])

# Ensure at least one detection exists
if len(indexes) > 0:
    # Loop over the indexes we are keeping
    for i in indexes.flatten():
        # Extract the bounding box coordinates
        (x, y) = (bounding_boxes[i][0], bounding_boxes[i][1])
        (w, h) = (bounding_boxes[i][2], bounding_boxes[i][3])

        # Draw a bounding box rectangle and label on the image
        color = [int(c) for c in COLORS[class_ids[i]]]
        cv2.rectangle(image, (x, y), (x + w, y + h), color, 2)
        text = "{}: {:.4f}".format(LABELS[class_ids[i]], confidences[i])
        cv2.putText(image, text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX,
                    0.5, color, 2)

# Show the output image
cv2.imshow("Image", image)
cv2.waitKey(0)
