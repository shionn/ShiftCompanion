#include <Arduino.h>
#include <Timer.h>

#include <lightstrip.h>
#include <display.h>

Display display = Display();
LightStrip lights = LightStrip();

Timer timer = Timer();

void update() {
	lights.state++;
}

void setup() {
	display.init();
	lights.init();
	Serial.begin(9600);
	timer.every(20, update);
}

void loop() {
	lights.draw();
	display.draw();
	timer.update();
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

			case 0xC0 : setTime(hour(), minute(), second(), day(), month(), 2000+c); cmd = 0xC1; break;
			case 0xC1 : setTime(hour(), minute(), second(), day(), c,       year()); cmd = 0xC2; break;
			case 0xC2 : setTime(hour(), minute(), second(), c,     month(), year()); cmd = 0xC3; break;
			case 0xC3 : setTime(c,      minute(), second(), day(), month(), year()); cmd = 0xC4; break;
			case 0xC4 : setTime(hour(), c,        second(), day(), month(), year()); cmd = 0xC5; break;
			case 0xC5 : setTime(hour(), minute(), c,        day(), month(), year()); cmd = 0x00; break;

			case 0xFF : lights.mode = c; cmd = 0x00; break;
			case 0x00 : cmd = c;    break;
			default   : cmd = 0x00; break;
		}
	}
}
