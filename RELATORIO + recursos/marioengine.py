from nes_py.wrappers import BinarySpaceToDiscreteSpaceEnv
import gym_super_mario_bros
from gym_super_mario_bros.actions import SIMPLE_MOVEMENT, COMPLEX_MOVEMENT
import json

#env = gym_super_mario_bros.make('SuperMarioBros-v3')
#env = BinarySpaceToDiscreteSpaceEnv(env, SIMPLE_MOVEMENT)

class MarioEngine:

	def run(self, solution, level, render, mode):
		env = gym_super_mario_bros.make(level)
		env = BinarySpaceToDiscreteSpaceEnv(env, COMPLEX_MOVEMENT)

		done = True
		reason_finish = "no_more_commands"

		pos = 0
		total_r = 0

		for step in range(len(solution)):
			if done:
				state = env.reset()

			state, reward, done, info = env.step(solution[pos])
			pos+=1


			if reward == -15: #faleceu
				reason_finish = "death"
				break

			if mode == "level" and info['flag_get'] == True:
				reason_finish = "win"
				break

			total_r = total_r + reward
			if render == "true":
				env.render()


		env.close()
		return total_r, pos, info, reason_finish
