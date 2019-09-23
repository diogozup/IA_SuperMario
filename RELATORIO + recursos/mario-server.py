import cherrypy
import marioengine
import json
import datetime
#import pandas as pd
#import myprocessor

#p = myprocessor.MyProcessor()
#engine = marioengine.MarioEngine()

class MyWebService(object):

	@cherrypy.expose
	@cherrypy.tools.json_out()
	@cherrypy.tools.json_in()
	def process(self):
		engine = marioengine.MarioEngine()
		data = cherrypy.request.json
		reward, pos, info, reason_finish = engine.run(data['solution'], data['level'], data['render'], data['mode'])
		res = {}
		res['reward'] = str(reward)
		res['reason_finish'] = str(reason_finish)
		res['coins'] = str(info['coins'])
		res['score'] = str(info['score'])
		res['commands_used'] = str(pos)
		res['time_left'] = str(info['time'])
		res['x_pos'] = str(info['x_pos'])
		res['world'] = str(info['world'])
		res['stage'] = str(info['stage'])
		res['solution'] = data['solution']
		res['timestamp'] = str(datetime.datetime.now())

		return json.dumps(res)

if __name__ == '__main__':
	config = {'server.socket_host': '0.0.0.0'}
	cherrypy.config.update(config)
	cherrypy.quickstart(MyWebService())
