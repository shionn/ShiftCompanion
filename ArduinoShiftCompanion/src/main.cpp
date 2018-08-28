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

Ssd1309 lcd = Ssd1309(LCD_CS, LCD_RW, LCD_RS);
Adafruit_NeoPixel strip = Adafruit_NeoPixel(64, STRIP_PIN, NEO_GRB + NEO_KHZ800);

uint8_t smilley[8] = {
	0b01111110,
	0b10000001,
	0b10100101,
	0b10000001,
	0b10100101,
	0b10011001,
	0b10000001,
	0b01111110
};

uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return strip.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}

void rainbow(uint8_t state) {
	uint16_t i;
	for(i=0; i<strip.numPixels(); i++) {
		strip.setPixelColor(i, Wheel((i+state) & 255));
	}
	strip.show();
}


void setup() {
	lcd.init();
	strip.begin();
	strip.show();
}

uint8_t stripStep = 0;

int x=1;
int y=1;
int dx = 2;
int dy = 2;

void loop() {

	rainbow(stripStep++);

	lcd.clearBuffer();
	lcd.print(2,2, "Bonjour!");

	lcd.hline(x-1,x+1, y-1);
	lcd.hline(x-1,x+1, y);
	lcd.hline(x-1,x+1, y+1);

	lcd.hline(0, 127, 0);
	lcd.hline(0, 127, 63);
	lcd.vline(0, 0, 63);
	lcd.vline(127, 0, 63);

	lcd.line(10,20,120,50);
	lcd.line(20,15,40,55);

	lcd.sprite(90,15,8,8,smilley);

	lcd.display();

	x+=dx;
	y+=dy;
	if (x>=125) dx=-1;
	if (x<=2) dx=1;
	if (y>=61) dy=-1;
	if (y<=2) dy = 1;
}
