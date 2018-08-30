
#ifndef __LIGHT_STRIP__
#define __LIGHT_STRIP__

#include <Adafruit_NeoPixel.h>

#define STRIP_PIN 4
#define STRIP_LEN 64

#define STRIP_MODE_RAINDOW 0
#define STRIP_MODE_THEATRE 1


class LightStrip {
	public :
		void init();
		void draw();
		uint8_t mode = STRIP_MODE_THEATRE;
		uint8_t state = 0;

	private :
		Adafruit_NeoPixel strip = Adafruit_NeoPixel(STRIP_LEN, STRIP_PIN, NEO_GRB + NEO_KHZ800);

		void rainbow();
		void theatre();

		uint32_t wheel(uint8_t state);
};

#endif
