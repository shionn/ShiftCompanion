#include <Arduino.h>
#include <ssd1309.h>
#include <Adafruit_NeoPixel.h>

// CS ou SS ou CHIP SELECT : RS
#define LCD_CS 10
// DS
#define LCD_RW 9
// DATA ou MOSI ou SDA : R/W
// #define LCD_DATA 11
// CLOCK, E, SCL
// #define CLOCK 13
// Reset, RS
#define LCD_RS 8

// pin du strip
#define STRIP_PIN 4
#define STRIP_LEN 64

#define STRIP_MODE_RAINDOW 0
#define STRIP_MODE_THEATRE 1

Ssd1309 lcd = Ssd1309(LCD_CS, LCD_RW, LCD_RS);
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
	lcd.init();
	strip.begin();
	strip.show();
	Serial.begin(9600);
}

uint8_t cmd = 0x00;

uint8_t cpuTemp = 0x00;

uint8_t stripMode = STRIP_MODE_RAINDOW;

uint8_t x = 2;
uint8_t y = 2;
int8_t dx = 1;
int8_t dy = 1;

void loop() {
	switch (stripMode) {
		case STRIP_MODE_RAINDOW : rainbow(stripStep++); break;
		case STRIP_MODE_THEATRE : theatre(stripStep++); break;
	}

	lcd.clearBuffer();
	lcd.print(2,  2, "Cpu: " + String(cpuTemp));

	lcd.hline(x-1,x+1, y-1);
	lcd.hline(x-1,x+1, y);
	lcd.hline(x-1,x+1, y+1);

	lcd.hline(0, 127, 0);
	lcd.hline(0, 127, 63);
	lcd.vline(0, 0, 63);
	lcd.vline(127, 0, 63);

	lcd.display();

	x+=dx;
	y+=dy;
	if (x>=125) dx=-1;
	if (x<=2) dx=1;
	if (y>=61) dy=-1;
	if (y<=2) dy = 1;
}


void serialEvent() {
	if (Serial.available()) {
		uint8_t c = (uint8_t)Serial.read();
		switch (cmd) {
			case 'T'  : cpuTemp   = c; cmd = 0x00; break;
			case 'L'  : stripMode = c; cmd = 0x00; break;
			case 0x00 : cmd = c; break;
			default   : cmd = 0x00; break;
		}
	}
}
