import pika
import uuid
import sys
import Image
import os
import io
from array import array

QUEUE_URL="amqp://jagruti:jagruti@192.168.1.111:5672"
GET = "get"
PUT = "put"
POST = "post"
DELETE = "delete"
INBOUND_QUEUE = "inbound_queue"
GET_QUEUE = "get_queue"

class QueueRpcClient(object):
	def __init__(self):
		self.connection = pika.BlockingConnection(pika.URLParameters(QUEUE_URL))

		self.channel = self.connection.channel()

		result = self.channel.queue_declare(exclusive=True)
		self.callback_queue = result.method.queue

		self.channel.basic_consume(self.on_response, no_ack=True,
                                   queue=self.callback_queue)

	def on_response(self, ch, method, props, body):
		if self.corr_id == props.correlation_id:
			self.response = body

	def get(self,key):
		self.response = None
		self.corr_id = str(uuid.uuid4())
		self.channel.basic_publish(exchange='',
                                   routing_key=GET_QUEUE,properties=pika.BasicProperties( 
                                   		 type = GET,
                                         reply_to = self.callback_queue,
                                         correlation_id = self.corr_id,
                                         ),
                                   body=str(key))
		while self.response is None:
			self.connection.process_data_events()

		return str(self.response)



	def post(self,imageByteArray):
		self.response = None
		self.corr_id = str(uuid.uuid4())
		self.channel.basic_publish(exchange='',
                                   routing_key=INBOUND_QUEUE,
                                   properties=pika.BasicProperties(
                                   		 type = POST,
                                         reply_to = self.callback_queue,
                                         correlation_id = self.corr_id,

                                         ),
                                   body=str(imageByteArray))
		while self.response is None:
			self.connection.process_data_events()

		return self.response
		

	def delete(key):
		self.channel.basic_publish(exchange='',
                                   routing_key=INBOUND_QUEUE,
                                   properties=pika.BasicProperties(
                                   		 type = DELETE,
                                         ),
                                   body=imageByteArray)


	def put(key, imageByteArray):
		self.channel.basic_publish(exchange='',
                                   routing_key=INBOUND_QUEUE,
                                   properties=pika.BasicProperties(
                                   		 type = PUT,
                                         correlation_id = key,
                                         ),
                                   body=imageByteArray)





if __name__ == "__main__":
	#arr =get(key="12")
	print """
	------------------------------------------------------------------------------------------------------	
		Sample Commands to Python Client API
			
			***GET API***python python_client.py get 232dd-fdfs-fdfsf /home/vinit/picked_image.jpeg
			***POST API***python python_client.py post /home/vinit/toBePosted.jpeg
			***PUT API***python python_client.py put 232dd-fdfs-fdfsf /home/vinit/toBeUpdatedImage.jpeg
			***DELETE API***python python_client.py delete 232dd-fdfs-fdfsf
	-------------------------------------------------------------------------------------------------------

	"""

	queueRpcClient = QueueRpcClient()
	if(sys.argv[1]=="get"):
		key=sys.argv[2]
		savepath =sys.argv[3]
		print "Getting an image from Project Fluffy with key = "+key
		print "Image will be saved at this path =" +savepath
		response=queueRpcClient.get(key)
		image=Image.open(io.BytesIO(response))
		image.save(sys.argv[3])
		
	elif(sys.argv[1]=="post"):
		imagePath=sys.argv[2]
		print "Posting an image having path as = "+imagePath
		with open(imagePath, "rb") as imageFile:
			f = imageFile.read()
			imageByteArray = bytearray(f)
		key = queueRpcClient.post(imageByteArray)
		print "Image is posted and can be retrieved with key ="+key
	elif(sys.argv[1]=="put"):
		key=sys.argv[2]
		imagePath=sys.argv[3]
		print "Updating an image with key = " + key +" with a different image located at = " + imagePath
		with open(imagePath, "rb") as imageFile:
			f = imageFile.read()
			imageByteArray = bytearray(f)
		queueRpcClient.put(key,imageByteArray)

	elif(sys.argv[1]=="delete"):
		key=sys.argv[2]
		print "Deleting an image from Project Fluffy with key = "+key
		queueRpcClient.delete(key)

	print """

	Exiting the client .....

	"""
