#include "fans.h"

void Fans::init() {
	pinMode(FAN_PIN1, OUTPUT);
	pinMode(FAN_PIN2, OUTPUT);
	pinMode(FAN_PIN3, OUTPUT);
}

void Fans::update() {
	analogWrite(FAN_PIN1, speed[0]);
	analogWrite(FAN_PIN2, speed[1]);
	analogWrite(FAN_PIN3, speed[2]);
}

void Fans::setSpeed(uint8_t fan, uint8_t speed) {
	this->speed[fan] = speed;
}
