
#include "display.h"

void Display::init() {
	lcd.init();
}

void Display::draw() {
	lcd.clearBuffer();
	switch (mode) {
		case LCD_MODE_CLOCK  : drawClock();   break;
		case LCD_MODE_LOGO   : drawLogo();    break;
		case LCD_MODE_SYSTEM : drawSysInfo(); break;
		case LCD_MODE_PONG   : drawPong();    break;
	}

	lcd.hline(0, 127, 0);
	lcd.hline(0, 127, 63);
	lcd.vline(0, 0, 63);
	lcd.vline(127, 0, 63);

	lcd.display();
}

void Display::drawClock() {
	lcd.print(6, 4,
		(hour()<10?"0":"")+String(hour())
		+(second()%2?":":" ")+
		(minute()<10?"0":"")+String(minute()), 3);
	lcd.print(22, 52,
		String(dayShortStr(weekday())) + " " +
		(day()<10?"0":"")   + String(day())     + "/" +
		(month()<10?"0":"") + String(month())   + "/" +
		String(year()));
}

void Display::drawLogo() {
	// S
	lcd.hline( 5, 20, 5);
	lcd.hline( 5, 20, 6);
	lcd.vline( 5, 7, 10);
	lcd.vline( 6, 7, 10);
	for (int i=0;i<8;i++) {
		lcd.hline(5+i*2, 6+i*2, 11+i);
		lcd.hline(5+i*2, 6+i*2, 12+i);
	}
	lcd.vline(19, 20, 23);
	lcd.vline(20, 20, 23);
	lcd.hline( 5, 20, 24);
	lcd.hline( 5, 20, 25);

	// H
	lcd.vline(23,  5, 25);
	lcd.vline(24,  5, 25);
	lcd.hline(25, 36, 15);
	lcd.hline(25, 36, 16);
	lcd.vline(37,  5, 25);
	lcd.vline(38,  5, 25);

	// I
	lcd.vline(41,  5, 25);
	lcd.vline(42,  5, 25);

	// F
	lcd.vline(45,  5, 25);
	lcd.vline(46,  5, 25);
	lcd.hline(47, 60, 5);
	lcd.hline(47, 60, 6);
	lcd.hline(47, 60, 15);
	lcd.hline(47, 60, 16);

	// T
	lcd.hline(63, 78, 5);
	lcd.hline(63, 78, 6);
	lcd.vline(70,  7, 25);
	lcd.vline(71,  7, 25);

	lcd.print(71, 53, "by Shionn");
}

void Display::drawPong() {
	lcd.vline(63, 2, 61);
	lcd.vline(64, 2, 61);
	lcd.print(56, 2, String(p1c));
	lcd.print(65, 2, String(p2c));
	lcd.fillbox(x-1, y-1, x+1, y+1);


	lcd.vline(5,   p1y-3, p1y+3);
	lcd.vline(122, p2y-3, p2y+3);

	if (dx < 0 && x % 2) {
		if (p1y > y) p1y--;
		if (p1y < y) p1y++;
	}
	if (dx > 0 && x % 2) {
		if (p2y > y) p2y--;
		if (p2y < y) p2y++;
	}
	if (x == 5   && p1y+3 >= y && y >= p1y-3 ) dx = 1;
	if (x == 122 && p2y+3 >= y && y >= p2y-3 ) dx =-1;


	x+=dx;
	y+=dy;
	if (x>=127) {dx=-1; p1c++;}
	if (x<=0) {dx=1; p2c++;}
	if (y>=61) dy=-1;
	if (y<=2) dy = 1;

}

void Display::drawSysInfo() {
	lcd.print(2,  2,  "Cpu  : " + String(cpuTemp));
	lcd.print(90, 2,  "("+ String(sysLoad/10.0f)+")");
	lcd.print(2,  11, "Z97  : " + String(moboTemp));
	lcd.print(2,  20, "Pump : " + String(pumpSpeed*50));
	lcd.print(2,  29, "Case : " + String(caseSpeed*50));
}
