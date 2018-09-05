
#ifndef __DISPLAY__
#define __DISPLAY__

#include <ssd1309.h>
#include <Time.h>

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

#define LCD_MODE_LOGO 0
#define LCD_MODE_SYSTEM 1
#define LCD_MODE_CLOCK 2
#define LCD_MODE_PONG 3
#define LCD_MODE_NETWORK 4


class Display {
	public :
		void init();
		void draw();
		uint8_t mode = LCD_MODE_LOGO;

		uint8_t cpuTemp   = 0x00;
		uint8_t moboTemp  = 0x00;
		uint8_t sysLoad   = 0x00;
		uint8_t pumpSpeed = 0x00;
		uint8_t caseSpeed = 0x00;
		uint8_t memory    = 0x00;
	private :
		void drawClock();
		void drawLogo();
		void drawSysInfo();
		void drawPong();

		Ssd1309 lcd = Ssd1309(LCD_CS, LCD_RW, LCD_RS);
		uint8_t p1y = 32, p2y = 32;
		uint8_t p1c = 0,  p2c = 0;
		uint8_t x  = 63;
		uint8_t y  = 31;
		int8_t  dx = 1;
		int8_t  dy = 1;
};

#endif
