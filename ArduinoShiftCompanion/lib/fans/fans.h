#ifndef __FANS__
#define __FANS__

#include <Arduino.h>

#define FAN_PIN1 3
#define FAN_PIN2 5
#define DEFAULT_FAN_SPEED 128

class Fans {
	public :
		void init();
		void update();

		void setSpeed1(uint8_t speed);
		void setSpeed2(uint8_t speed);

	private :
		uint8_t speed1 = DEFAULT_FAN_SPEED;
		uint8_t speed2 = DEFAULT_FAN_SPEED;

};

#endif
