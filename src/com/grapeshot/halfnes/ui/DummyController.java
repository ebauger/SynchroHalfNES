/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grapeshot.halfnes.ui;

import static com.grapeshot.halfnes.utils.*;
import java.util.HashMap;

/**
 *
 * @author Andrew
 */
public class DummyController implements ControllerInterface {

    //i wrote this to test a bug in the menu of one game.
    //if using this again, maybe a parser and some RLE would be appropriate?
    //or just make it load FCEUX movie files?
    int outbyte = 0;
    int latchbyte = 0;
    char[] input = ("000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "00000000000000000000000000000SSSSSSSSSSSSSS00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "00000000000000000000000000000AAAAAAAAAAAAAA00000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000"
            + "00000000000000000000000000000SSSSSSSSSSSSSS00000000000000000000000").toCharArray();
    HashMap<Character, Integer> m = new HashMap<Character, Integer>();
    int frame = 0;

    public DummyController(int controllernum) {
        m.put('0', 0x00);
        m.put('U', BIT4);
        m.put('D', BIT5);
        m.put('L', BIT6);
        m.put('R', BIT7);
        m.put('A', BIT0);
        m.put('B', BIT1);
        m.put('s', BIT2);
        m.put('S', BIT3);
    }

    @Override
    public void strobe() {
        //shifts a byte out
        outbyte = latchbyte & 1;
        latchbyte = ((latchbyte >> 1) | 0x100);
    }

    @Override
    public int getbyte() {
        return outbyte;
    }

    @Override
    public void output(final boolean state) {
        if (frame < input.length) {
            latchbyte = m.get(input[frame]);
        } else {
            latchbyte = 0;
        }
        ++frame;
    }

	@Override
	public int getLatchByte() {
		// TODO Auto-generated method stub
		return 0;
	}
}
