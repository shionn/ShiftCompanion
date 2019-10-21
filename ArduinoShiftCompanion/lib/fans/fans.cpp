#include "fans.h"

void Fans::init() {
	pinMode(FAN_PIN1, OUTPUT);
	pinMode(FAN_PIN2, OUTPUT);
}

void Fans::update() {
	analogWrite(FAN_PIN1, speed1);
	analogWrite(FAN_PIN2, speed2);
}

void Fans::setSpeed1(uint8_t speed) {
	this->speed1 = speed;
}

void Fans::setSpeed2(uint8_t speed) {
	this->speed2 = speed;
}
