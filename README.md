# HardwareSim
Hardware simulator, use NAND gates to build your own computer chips!

Instructions on how to use the program
-------------------------------------------------------------
a blue node represents an input
a brown node respresents an output

a cable may only connect two nodes and cannot connect to other cables
if one wishes to split a cable into 2 new cables, one can use a splitter, denoted ass the orange square with black edges
a cable cannot, at this time, be different colors, as i've had them change to indicate wether or not there is current passing through

to choose a component, simply click on the desired component to the left and then click on the grey board to the right to indicate where the component should be placed down

to create a chip, click Chip -> create new, 
the inputs apear to the left, the output appears to the right.
The X in the top right corner cancels the current chip making, the C in the button right corner prompts you for a file name and a description after which the chip is saved to chDir

to load a chip, press Chip -> load a chip, after which the chip will be placed in your "hand" and can be placed like any other chip

a button, denoted by a red circle in a grey frame, can be clicked, after which it'll stay on for a preset static amount of time
a lamp, denoted by a grey circle, will turn on if the input is high, and will then proceed to pass the signal right through without modification to the signal, the same goes for a low input
a clock, denoted as a clock, switches on and off over a preset static amount of on-time and off-time. The cycle begins as the clock is taken from the menu
a lever, denoted by a green square, is a component that'll switch on or off, as it is clicked

a NAND gate is a... well, a NAND gate, what did you expect?
-------------------------------------------------------------
self resonence is a problem, it's quite likely if you reach (> 2 Mcalc/s)

Contributions are always welcome.
I can be reached at lukaschristensson@hotmail.com.

Good Luck!
