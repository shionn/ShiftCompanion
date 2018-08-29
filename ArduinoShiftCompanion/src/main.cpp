#include <Arduino.h>
#include <Adafruit_NeoPixel.h>

#include <display.h>

// pin du strip
#define STRIP_PIN 4
#define STRIP_LEN 64

#define STRIP_MODE_RAINDOW 0
#define STRIP_MODE_THEATRE 1


Display display = Display();

Adafruit_NeoPixel strip = Adafruit_NeoPixel(STRIP_LEN, STRIP_PIN, NEO_GRB + NEO_KHZ800);

uint32_t Wheel(uint8_t state) {
	if(state < 85) {
		return strip.Color(255 - state * 3, 0, state * 3);
	}
	if(state < 170) {
		state -= 85;
		return strip.Color(0, state * 3, 255 - state * 3);
	}
	state -= 170;
	return strip.Color(state * 3, 255 - state * 3, 0);
}

void rainbow(uint8_t state) {
	for(uint8_t i=0; i<STRIP_LEN; i++) {
		strip.setPixelColor(i, Wheel(i+state));
	}
	strip.show();
}

void theatre(uint8_t state) {
	for(uint8_t i=0; i<STRIP_LEN; i++) {
		strip.setPixelColor(i, Wheel(state));
	}
	strip.show();
}

uint8_t stripStep = 0;

void setup() {
	display.init();
	strip.begin();
	strip.show();
	Serial.begin(9600);
}



uint8_t stripMode = STRIP_MODE_RAINDOW;


void loop() {
	switch (stripMode) {
		case STRIP_MODE_RAINDOW : rainbow(stripStep++); break;
		case STRIP_MODE_THEATRE : theatre(stripStep++); break;
	}

	display.draw();
}

uint8_t cmd = 0x00;

void serialEvent() {
	if (Serial.available()) {
		uint8_t c = (uint8_t)Serial.read();
		switch (cmd) {
			case 0xA0 : display.cpuTemp   = c; cmd = 0xA1; break;
			case 0xA1 : display.moboTemp  = c; cmd = 0xA2; break;
			case 0xA2 : display.sysLoad   = c; cmd = 0xA3; break;
			case 0xA3 : display.pumpSpeed = c; cmd = 0xA4; break;
			case 0xA4 : display.caseSpeed = c; cmd = 0x00; break;

			case 0xB0 : display.mode      = c; cmd = 0x00; break;

			case 0xFF : stripMode = c; cmd = 0x00; break;
			case 0x00 : cmd = c;    break;
			default   : cmd = 0x00; break;
		}
	}
}
