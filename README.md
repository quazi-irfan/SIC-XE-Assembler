# Comparing C(x86) compile + linking with [SIC](https://en.wikipedia.org/wiki/Simplified_Instructional_Computer)-XE assembler + linking + loading


## C(x86)
main.c contains

```C
#include<stdio.h>

extern int fact(int i);

int main(void){
    int i = fact(5);
    printf("5 Factorial is %d\n", i);
    return 0;
}
```

fact.c contains:
```C

int fact(int i){
    if(i == 0) 
        return 1;
    else
        return i * fact(i-1); 
}

```

Compile with debug information(to see C soruce and assembly interleved) and use objdump to see the generated object code: main.o and fact.o

```sh
$ gcc -c -g main.c 
$ objdump -dS main.o

main.o:     file format elf64-x86-64
Disassembly of section .text:

0000000000000000 <main>:
#include<stdio.h>

extern int fact(int i);

int main(void){
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 83 ec 10          	sub    $0x10,%rsp
    int i = fact(5);
   8:	bf 05 00 00 00       	mov    $0x5,%edi
   d:	e8 00 00 00 00       	callq  12 <main+0x12>
  12:	89 45 fc             	mov    %eax,-0x4(%rbp)
    printf("5 Factorial is %d\n", i);
  15:	8b 45 fc             	mov    -0x4(%rbp),%eax
  18:	89 c6                	mov    %eax,%esi
  1a:	bf 00 00 00 00       	mov    $0x0,%edi
  1f:	b8 00 00 00 00       	mov    $0x0,%eax
  24:	e8 00 00 00 00       	callq  29 <main+0x29>
    return 0;
  29:	b8 00 00 00 00       	mov    $0x0,%eax
}
  2e:	c9                   	leaveq 
  2f:	c3                   	retq  


$ gcc -c -g fact.c
$ objdump -dS fact.o

fact.o:     file format elf64-x86-64
Disassembly of section .text:

0000000000000000 <fact>:
int fact(int i){
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 83 ec 10          	sub    $0x10,%rsp
   8:	89 7d fc             	mov    %edi,-0x4(%rbp)
    if(i == 0) 
   b:	83 7d fc 00          	cmpl   $0x0,-0x4(%rbp)
   f:	75 07                	jne    18 <fact+0x18>
        return 1;
  11:	b8 01 00 00 00       	mov    $0x1,%eax
  16:	eb 11                	jmp    29 <fact+0x29>
    else
        return i * fact(i-1); 
  18:	8b 45 fc             	mov    -0x4(%rbp),%eax
  1b:	83 e8 01             	sub    $0x1,%eax
  1e:	89 c7                	mov    %eax,%edi
  20:	e8 00 00 00 00       	callq  25 <fact+0x25>
  25:	0f af 45 fc          	imul   -0x4(%rbp),%eax
}
  29:	c9                   	leaveq 
  2a:	c3                   	retq   
```

Lets invoke the linker to create the executable program, and use objdump again to see the object code from main and fact inside the executable.

```sh
$ gcc main.o fact.o

a.out:     file format elf64-x86-64
Disassembly of section .init:

00000000004003c8 <_init>:
  4003c8:	48 83 ec 08          	sub    $0x8,%rsp
  4003cc:	48 8b 05 25 0c 20 00 	mov    0x200c25(%rip),%rax        
  4003d3:	48 85 c0             	test   %rax,%rax
  4003d6:	74 05                	je     4003dd <_init+0x15>
  .
  .
  .
  40051b:	48 89 e5             	mov    %rsp,%rbp
  40051e:	ff d0                	callq  *%rax
  400520:	5d                   	pop    %rbp
  400521:	e9 7a ff ff ff       	jmpq   4004a0 <register_tm_clones>

0000000000400526 <main>:										# main function
  400526:	55                   	push   %rbp
  400527:	48 89 e5             	mov    %rsp,%rbp
  40052a:	48 83 ec 10          	sub    $0x10,%rsp
  40052e:	bf 05 00 00 00       	mov    $0x5,%edi
  400533:	e8 1e 00 00 00       	callq  400556 <fact>
  400538:	89 45 fc             	mov    %eax,-0x4(%rbp)
  40053b:	8b 45 fc             	mov    -0x4(%rbp),%eax
  40053e:	89 c6                	mov    %eax,%esi
  400540:	bf 14 06 40 00       	mov    $0x400614,%edi
  400545:	b8 00 00 00 00       	mov    $0x0,%eax
  40054a:	e8 b1 fe ff ff       	callq  400400 <printf@plt>
  40054f:	b8 00 00 00 00       	mov    $0x0,%eax
  400554:	c9                   	leaveq 
  400555:	c3                   	retq   

0000000000400556 <fact>:										# fact function
  400556:	55                   	push   %rbp
  400557:	48 89 e5             	mov    %rsp,%rbp
  40055a:	48 83 ec 10          	sub    $0x10,%rsp
  40055e:	89 7d fc             	mov    %edi,-0x4(%rbp)
  400561:	83 7d fc 00          	cmpl   $0x0,-0x4(%rbp)
  400565:	75 07                	jne    40056e <fact+0x18>
  400567:	b8 01 00 00 00       	mov    $0x1,%eax
  40056c:	eb 11                	jmp    40057f <fact+0x29>
  40056e:	8b 45 fc             	mov    -0x4(%rbp),%eax
  400571:	83 e8 01             	sub    $0x1,%eax
  400574:	89 c7                	mov    %eax,%edi
  400576:	e8 db ff ff ff       	callq  400556 <fact>
  40057b:	0f af 45 fc          	imul   -0x4(%rbp),%eax
  40057f:	c9                   	leaveq 
  400580:	c3                   	retq   
  400581:	66 2e 0f 1f 84 00 00 	nopw   %cs:0x0(%rax,%rax,1)
  400588:	00 00 00 
  40058b:	0f 1f 44 00 00       	nopl   0x0(%rax,%rax,1)

0000000000400590 <__libc_csu_init>:
  400590:	41 57                	push   %r15
  400592:	41 56                	push   %r14
  400594:	41 89 ff             	mov    %edi,%r15d
  .
  .
  .

0000000000400604 <_fini>:
  400604:	48 83 ec 08          	sub    $0x8,%rsp
  400608:	48 83 c4 08          	add    $0x8,%rsp
  40060c:	c3                   	retq 
```

To see the symbol table we will invoke nm a.out
```sh
$ nm a.out
0000000000601038 B __bss_start
0000000000601038 b completed.7585
0000000000601028 D __data_start
0000000000601028 W data_start
0000000000400460 t deregister_tm_clones
00000000004004e0 t __do_global_dtors_aux
0000000000600e18 t __do_global_dtors_aux_fini_array_entry
0000000000601030 D __dso_handle
0000000000600e28 d _DYNAMIC
0000000000601038 D _edata
0000000000601040 B _end
0000000000400556 T fact
0000000000400604 T _fini
0000000000400500 t frame_dummy
0000000000600e10 t __frame_dummy_init_array_entry
0000000000400778 r __FRAME_END__
0000000000601000 d _GLOBAL_OFFSET_TABLE_
                 w __gmon_start__
0000000000400628 r __GNU_EH_FRAME_HDR
00000000004003c8 T _init
0000000000600e18 t __init_array_end
0000000000600e10 t __init_array_start
0000000000400610 R _IO_stdin_used
                 w _ITM_deregisterTMCloneTable
                 w _ITM_registerTMCloneTable
0000000000600e20 d __JCR_END__
0000000000600e20 d __JCR_LIST__
                 w _Jv_RegisterClasses
0000000000400600 T __libc_csu_fini
0000000000400590 T __libc_csu_init
                 U __libc_start_main@@GLIBC_2.2.5
0000000000400526 T main								
                 U printf@@GLIBC_2.2.5
00000000004004a0 t register_tm_clones
0000000000400430 T _start
0000000000601038 D __TMC_END__
```
Here you will note that main is at location 400526 and fact is at location 400556. The T in the second column means they are in the text section of our program. Which matches with our object dump.



## SIC-XE

main.asm contains,
```SIC
PROG		START		0
            EXTDEF		THREE
            EXTREF		ONE,TWO,FUNC
FIRST		+LDA		ONE
LOOP		+STA		TWO+3
            +J			LOOP
            +JSUB		FUNC
THREE		WORD		TWO-ONE
            END			FIRST
```
func.asm contains,
```SIC	
FUNC	START		0
		EXTDEF		ONE,TWO		
		EXTREF		THREE		
LOOP	+STA		THREE
		J		    LOOP
		RSUB					
ONE		WORD		8			
TWO		RESW		3			
FOUR	WORD		ONE
		END		    LOOP
```

Compile the assembler first, and then feed the asm files in the assembler.
```sh
$ javac Assembler/Main.java
$ java Assembler.Main main.asm
```

It will generate object code main.o
```sh
Reading from File : main.asm

*********** PASS 1 ***********

> Generated Intermediate File : main.int
00000   PROG        START               0
00000               EXTDEF              THREE
00000               EXTREF              ONE,TWO,FUNC
00000   FIRST       +LDA                ONE
00004   LOOP        +STA                TWO+3
00008               +J                  LOOP
0000C               +JSUB               FUNC
00010   THREE       WORD                TWO-ONE
00013               END                 FIRST

> Symbol Table
Symbol  Value   rflag   iflag   mflag
FIRS     00000     1       1       0
LOOP     00004     1       1       0
PROG     00000     1       1       0
THRE     00010     1       1       0

> Literal Table
Literal                 Value           length  address
(Literal Table is Empty)

Continue to see the output of Pass 2.


*********** PASS 2 ***********

> Adding object code to Intermediate File : main.txt
00000   PROG        START               0
00000               EXTDEF              THREE
00000               EXTREF              ONE,TWO,FUNC
00000   FIRST       +LDA                ONE                 03100000
00004   LOOP        +STA                TWO+3               0F100003
00008               +J                  LOOP                3F100004
0000C               +JSUB               FUNC                4B100000
00010   THREE       WORD                TWO-ONE             000000
00013               END                 FIRST

Continue to see the Updated Symbol table and Object Code

> Updated Symbol Table
Symbol  Value   rflag   iflag   mflag
FIRS     00000     1       1       0
FUNC     00000     0       0       0
LOOP     00004     1       1       0
ONE      00000     0       0       0
PROG     00000     1       1       0
THRE     00010     1       1       0
TWO      00000     0       0       0

> Generated Object Code : main.o
H^PROG^000000^000013
D^THRE^000010
R^ONE ^TWO ^FUNC
T^000000^13^03100000^0F100003^3F100004^4B100000^000000
M^000001^05^+ONE
M^000005^05^+TWO
M^000009^05^+PROG
M^00000D^05^+FUNC
M^000010^06^-ONE
M^000010^06^+TWO
E^000000
```

Feed func.asm to the assembler, and the assembler will generate func.o
```sh
Reading from File : func.asm

*********** PASS 1 ***********

> Generated Intermediate File : func.int
00000   FUNC        START               0
00000               EXTDEF              ONE,TWO
00000               EXTREF              THREE
00000   LOOP        +STA                THREE
00004               J                   LOOP
00007               RSUB
0000A   ONE         WORD                8
0000D   TWO         RESW                3
00016   FOUR        WORD                ONE
00019               END                 LOOP

> Symbol Table
Symbol  Value   rflag   iflag   mflag
FOUR     00016     1       1       0
FUNC     00000     1       1       0
LOOP     00000     1       1       0
ONE      0000A     1       1       0
TWO      0000D     1       1       0

> Literal Table
Literal                 Value           length  address
(Literal Table is Empty)

Continue to see the output of Pass 2.


*********** PASS 2 ***********

> Adding object code to Intermediate File : func.txt
00000   FUNC        START               0
00000               EXTDEF              ONE,TWO
00000               EXTREF              THREE
00000   LOOP        +STA                THREE               0F100000
00004               J                   LOOP                3F2FF9
00007               RSUB                                    4F0000
0000A   ONE         WORD                8                   000008
0000D   TWO         RESW                3
00016   FOUR        WORD                ONE                 00000A
00019               END                 LOOP

Continue to see the Updated Symbol table and Object Code

> Updated Symbol Table
Symbol  Value   rflag   iflag   mflag
FOUR     00016     1       1       0
FUNC     00000     1       1       0
LOOP     00000     1       1       0
ONE      0000A     1       1       0
THRE     00000     0       0       0
TWO      0000D     1       1       0

> Generated Object Code : func.o
H^FUNC^000000^000019
D^ONE ^00000A^TWO ^00000D
R^THRE
T^000000^0D^0F100000^3F2FF9^4F0000^000008
T^000016^03^00000A
M^000001^05^+THRE
M^000016^06^+FUNC
E^000000
```

We have our linker and loader invoked together. We will see the machine code laied out in the memory. The excution will start from 3860. The symbol table is added at the end.

```sh
$ java LinkerLoader.Main main.o func.o
Memory Layout :
      0  1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
386X 03 10 38 7d 0F 10 38 83 3F 10 38 64 4B 10 38 73
387X 10 03 84 0F 10 38 70 3F 2F F9 4F 00 00 00 00 08
388X ?? ?? ?? ?? ?? ?? ?? ?? ?? 00 38 7d ?? ?? ?? ??

Execution Address : 3860

External Symbol Table :
CSECT   SYMBOL  ADDR    CSADDR  LDADDR  LENGTH
FUNC                     3873              19
PROG                     3860              13
        THRE      10              3870
         ONE       a              387d
         TWO       d              3880
```
