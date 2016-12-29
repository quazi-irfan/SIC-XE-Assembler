# Comparing C(x86) compile + linking with SIC-XE assembler + linking + loading


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

Compile and use objdump to see the generated object code: main.o and fact.o

```sh
$ gcc -c main.c > main.o
$ objdump -d main.o

main.o:     file format elf64-x86-64
Disassembly of section .text:
0000000000000000 <main>:
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 83 ec 10          	sub    $0x10,%rsp
   8:	bf 05 00 00 00       	mov    $0x5,%edi
   d:	e8 00 00 00 00       	callq  12 <main+0x12>
  12:	89 45 fc             	mov    %eax,-0x4(%rbp)
  15:	8b 45 fc             	mov    -0x4(%rbp),%eax
  18:	89 c6                	mov    %eax,%esi
  1a:	bf 00 00 00 00       	mov    $0x0,%edi
  1f:	b8 00 00 00 00       	mov    $0x0,%eax
  24:	e8 00 00 00 00       	callq  29 <main+0x29>
  29:	b8 00 00 00 00       	mov    $0x0,%eax
  2e:	c9                   	leaveq 
  2f:	c3                   	retq   


$ gcc -c fact.c > fact.o
$ objdump -d fact.o

fact.o:     file format elf64-x86-64
Disassembly of section .text:
0000000000000000 <fact>:
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 83 ec 10          	sub    $0x10,%rsp
   8:	89 7d fc             	mov    %edi,-0x4(%rbp)
   b:	83 7d fc 00          	cmpl   $0x0,-0x4(%rbp)
   f:	75 07                	jne    18 <fact+0x18>
  11:	b8 01 00 00 00       	mov    $0x1,%eax
  16:	eb 11                	jmp    29 <fact+0x29>
  18:	8b 45 fc             	mov    -0x4(%rbp),%eax
  1b:	83 e8 01             	sub    $0x1,%eax
  1e:	89 c7                	mov    %eax,%edi
  20:	e8 00 00 00 00       	callq  25 <fact+0x25>
  25:	0f af 45 fc          	imul   -0x4(%rbp),%eax
  29:	c9                   	leaveq 
  2a:	c3                   	retq   
```

Lets invoke the linker to create the executable program, and use objdump again to see the object code from main and fact inside the executable.

```sh
$ gcc main.o fact.o > a.out

a.out:     file format elf64-x86-64
Disassembly of section .init:

00000000004003c8 <_init>:
  4003c8:	48 83 ec 08          	sub    $0x8,%rsp
  4003cc:	48 8b 05 25 0c 20 00 	mov    0x200c25(%rip),%rax        # 600ff8 <_DYNAMIC+0x1d0>
  4003d3:	48 85 c0             	test   %rax,%rax
  4003d6:	74 05                	je     4003dd <_init+0x15>
  4003d8:	e8 43 00 00 00       	callq  400420 <__libc_start_main@plt+0x10>
  4003dd:	48 83 c4 08          	add    $0x8,%rsp
  4003e1:	c3                   	retq   

Disassembly of section .plt:

00000000004003f0 <printf@plt-0x10>:
  4003f0:	ff 35 12 0c 20 00    	pushq  0x200c12(%rip)        # 601008 <_GLOBAL_OFFSET_TABLE_+0x8>
  4003f6:	ff 25 14 0c 20 00    	jmpq   *0x200c14(%rip)        # 601010 <_GLOBAL_OFFSET_TABLE_+0x10>
  4003fc:	0f 1f 40 00          	nopl   0x0(%rax)

0000000000400400 <printf@plt>:
  400400:	ff 25 12 0c 20 00    	jmpq   *0x200c12(%rip)        # 601018 <_GLOBAL_OFFSET_TABLE_+0x18>
  400406:	68 00 00 00 00       	pushq  $0x0
  40040b:	e9 e0 ff ff ff       	jmpq   4003f0 <_init+0x28>

0000000000400410 <__libc_start_main@plt>:
  400410:	ff 25 0a 0c 20 00    	jmpq   *0x200c0a(%rip)        # 601020 <_GLOBAL_OFFSET_TABLE_+0x20>
  400416:	68 01 00 00 00       	pushq  $0x1
  40041b:	e9 d0 ff ff ff       	jmpq   4003f0 <_init+0x28>

Disassembly of section .plt.got:

0000000000400420 <.plt.got>:
  400420:	ff 25 d2 0b 20 00    	jmpq   *0x200bd2(%rip)        # 600ff8 <_DYNAMIC+0x1d0>
  400426:	66 90                	xchg   %ax,%ax

Disassembly of section .text:

0000000000400430 <_start>:
  400430:	31 ed                	xor    %ebp,%ebp
  400432:	49 89 d1             	mov    %rdx,%r9
  400435:	5e                   	pop    %rsi
  400436:	48 89 e2             	mov    %rsp,%rdx
  400439:	48 83 e4 f0          	and    $0xfffffffffffffff0,%rsp
  40043d:	50                   	push   %rax
  40043e:	54                   	push   %rsp
  40043f:	49 c7 c0 00 06 40 00 	mov    $0x400600,%r8
  400446:	48 c7 c1 90 05 40 00 	mov    $0x400590,%rcx
  40044d:	48 c7 c7 26 05 40 00 	mov    $0x400526,%rdi
  400454:	e8 b7 ff ff ff       	callq  400410 <__libc_start_main@plt>
  400459:	f4                   	hlt    
  40045a:	66 0f 1f 44 00 00    	nopw   0x0(%rax,%rax,1)

0000000000400460 <deregister_tm_clones>:
  400460:	b8 3f 10 60 00       	mov    $0x60103f,%eax
  400465:	55                   	push   %rbp
  400466:	48 2d 38 10 60 00    	sub    $0x601038,%rax
  40046c:	48 83 f8 0e          	cmp    $0xe,%rax
  400470:	48 89 e5             	mov    %rsp,%rbp
  400473:	76 1b                	jbe    400490 <deregister_tm_clones+0x30>
  400475:	b8 00 00 00 00       	mov    $0x0,%eax
  40047a:	48 85 c0             	test   %rax,%rax
  40047d:	74 11                	je     400490 <deregister_tm_clones+0x30>
  40047f:	5d                   	pop    %rbp
  400480:	bf 38 10 60 00       	mov    $0x601038,%edi
  400485:	ff e0                	jmpq   *%rax
  400487:	66 0f 1f 84 00 00 00 	nopw   0x0(%rax,%rax,1)
  40048e:	00 00 
  400490:	5d                   	pop    %rbp
  400491:	c3                   	retq   
  400492:	0f 1f 40 00          	nopl   0x0(%rax)
  400496:	66 2e 0f 1f 84 00 00 	nopw   %cs:0x0(%rax,%rax,1)
  40049d:	00 00 00 

00000000004004a0 <register_tm_clones>:
  4004a0:	be 38 10 60 00       	mov    $0x601038,%esi
  4004a5:	55                   	push   %rbp
  4004a6:	48 81 ee 38 10 60 00 	sub    $0x601038,%rsi
  4004ad:	48 c1 fe 03          	sar    $0x3,%rsi
  4004b1:	48 89 e5             	mov    %rsp,%rbp
  4004b4:	48 89 f0             	mov    %rsi,%rax
  4004b7:	48 c1 e8 3f          	shr    $0x3f,%rax
  4004bb:	48 01 c6             	add    %rax,%rsi
  4004be:	48 d1 fe             	sar    %rsi
  4004c1:	74 15                	je     4004d8 <register_tm_clones+0x38>
  4004c3:	b8 00 00 00 00       	mov    $0x0,%eax
  4004c8:	48 85 c0             	test   %rax,%rax
  4004cb:	74 0b                	je     4004d8 <register_tm_clones+0x38>
  4004cd:	5d                   	pop    %rbp
  4004ce:	bf 38 10 60 00       	mov    $0x601038,%edi
  4004d3:	ff e0                	jmpq   *%rax
  4004d5:	0f 1f 00             	nopl   (%rax)
  4004d8:	5d                   	pop    %rbp
  4004d9:	c3                   	retq   
  4004da:	66 0f 1f 44 00 00    	nopw   0x0(%rax,%rax,1)

00000000004004e0 <__do_global_dtors_aux>:
  4004e0:	80 3d 51 0b 20 00 00 	cmpb   $0x0,0x200b51(%rip)        # 601038 <__TMC_END__>
  4004e7:	75 11                	jne    4004fa <__do_global_dtors_aux+0x1a>
  4004e9:	55                   	push   %rbp
  4004ea:	48 89 e5             	mov    %rsp,%rbp
  4004ed:	e8 6e ff ff ff       	callq  400460 <deregister_tm_clones>
  4004f2:	5d                   	pop    %rbp
  4004f3:	c6 05 3e 0b 20 00 01 	movb   $0x1,0x200b3e(%rip)        # 601038 <__TMC_END__>
  4004fa:	f3 c3                	repz retq 
  4004fc:	0f 1f 40 00          	nopl   0x0(%rax)

0000000000400500 <frame_dummy>:
  400500:	bf 20 0e 60 00       	mov    $0x600e20,%edi
  400505:	48 83 3f 00          	cmpq   $0x0,(%rdi)
  400509:	75 05                	jne    400510 <frame_dummy+0x10>
  40050b:	eb 93                	jmp    4004a0 <register_tm_clones>
  40050d:	0f 1f 00             	nopl   (%rax)
  400510:	b8 00 00 00 00       	mov    $0x0,%eax
  400515:	48 85 c0             	test   %rax,%rax
  400518:	74 f1                	je     40050b <frame_dummy+0xb>
  40051a:	55                   	push   %rbp
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
  400597:	41 55                	push   %r13
  400599:	41 54                	push   %r12
  40059b:	4c 8d 25 6e 08 20 00 	lea    0x20086e(%rip),%r12        # 600e10 <__frame_dummy_init_array_entry>
  4005a2:	55                   	push   %rbp
  4005a3:	48 8d 2d 6e 08 20 00 	lea    0x20086e(%rip),%rbp        # 600e18 <__init_array_end>
  4005aa:	53                   	push   %rbx
  4005ab:	49 89 f6             	mov    %rsi,%r14
  4005ae:	49 89 d5             	mov    %rdx,%r13
  4005b1:	4c 29 e5             	sub    %r12,%rbp
  4005b4:	48 83 ec 08          	sub    $0x8,%rsp
  4005b8:	48 c1 fd 03          	sar    $0x3,%rbp
  4005bc:	e8 07 fe ff ff       	callq  4003c8 <_init>
  4005c1:	48 85 ed             	test   %rbp,%rbp
  4005c4:	74 20                	je     4005e6 <__libc_csu_init+0x56>
  4005c6:	31 db                	xor    %ebx,%ebx
  4005c8:	0f 1f 84 00 00 00 00 	nopl   0x0(%rax,%rax,1)
  4005cf:	00 
  4005d0:	4c 89 ea             	mov    %r13,%rdx
  4005d3:	4c 89 f6             	mov    %r14,%rsi
  4005d6:	44 89 ff             	mov    %r15d,%edi
  4005d9:	41 ff 14 dc          	callq  *(%r12,%rbx,8)
  4005dd:	48 83 c3 01          	add    $0x1,%rbx
  4005e1:	48 39 eb             	cmp    %rbp,%rbx
  4005e4:	75 ea                	jne    4005d0 <__libc_csu_init+0x40>
  4005e6:	48 83 c4 08          	add    $0x8,%rsp
  4005ea:	5b                   	pop    %rbx
  4005eb:	5d                   	pop    %rbp
  4005ec:	41 5c                	pop    %r12
  4005ee:	41 5d                	pop    %r13
  4005f0:	41 5e                	pop    %r14
  4005f2:	41 5f                	pop    %r15
  4005f4:	c3                   	retq   
  4005f5:	90                   	nop
  4005f6:	66 2e 0f 1f 84 00 00 	nopw   %cs:0x0(%rax,%rax,1)
  4005fd:	00 00 00 

0000000000400600 <__libc_csu_fini>:
  400600:	f3 c3                	repz retq 

Disassembly of section .fini:

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
