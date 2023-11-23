from flask import Flask, request

app = Flask(__name__)


@app.route('/dropoff/update-waiting', methods=['PUT'])
def receive_results():
    data = request.json  # Assuming the data is sent as JSON
    print("Received data:", data)

    # Add your processing logic here

    return "Data received successfully", 200


if __name__ == '__main__':
    app.run(host='http://54.180.118.50', port=8000)
