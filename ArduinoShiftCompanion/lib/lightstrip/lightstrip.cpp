#include "lightstrip.h"

void LightStrip::init() {
	strip.begin();
	strip.show();
}

void LightStrip::draw() {
	switch (mode) {
		case STRIP_MODE_RAINDOW : rainbow(); break;
		case STRIP_MODE_THEATRE : theatre(); break;
	}
	strip.show();
}

void LightStrip::rainbow() {
	for(uint8_t i=0; i<STRIP_LEN; i++) {
		strip.setPixelColor(i, wheel(i+state));
	}
}

void LightStrip::theatre() {
	for(uint8_t i=0; i<STRIP_LEN; i++) {
		strip.setPixelColor(i, wheel(state));
	}
}

uint32_t LightStrip::wheel(uint8_t state) {
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
