#ifndef __FANS__
#define __FANS__

#include <Arduino.h>

#define FAN_PIN1 3
#define FAN_PIN2 5
#define FAN_PIN3 6
#define DEFAULT_FAN_SPEED 128

class Fans {
	public :
		void init();
		void update();

		void setSpeed(uint8_t fan, uint8_t speed);

	private :
		uint8_t speed[3] = {DEFAULT_FAN_SPEED,DEFAULT_FAN_SPEED,DEFAULT_FAN_SPEED};

};

#endif
