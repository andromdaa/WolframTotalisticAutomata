# Project 2: Not-So-Elementary Cellular Automata

> The key to maximizing reuse lies in anticipating new requirements and changes to existing requirements...
> To design a system so that it's robust to such changes, you must consider how the system might need to change over its lifetime.
> A design that doesn't take change into account risks major redesign in the future.
>
> &mdash;Gang of Four, in [*Design Patterns: Elements of Reusable Object-Oriented Software*](https://en.wikipedia.org/wiki/Design_Patterns)

## Introduction

In Project 1, we wrote three classes to simulate the evolution of [elementary cellular automata](https://en.wikipedia.org/wiki/Elementary_cellular_automaton).
The goal of this project is to simulate a second type of one-dimensional, two-state cellular automaton while writing a minimal amount of new code and avoiding duplicate code.
In the process, we will modify our Project 1 classes to make it possible to simulate even more types of cellular automata in the future without changing our Project 2 code.

## Overview

A UML diagram of the classes is shown below.
To view a larger version, open the file [uml.pdf](./uml.pdf) in a PDF reader.

![UML diagram](./uml.svg)

As in Project 1, Automaton uses an ArrayList of Generations and a Rule to simulate a one-dimensional (1D), two-state cellular automaton (CA).
However, Automaton and Rule are now abstract and have the following abstract methods:

* `createRule(int ruleNum)`
* `evolve(boolean[] neighborhood)`
* `getNeighborhood(int idx, Generation gen)`
* `getRuleTable(char falseSymbol, char trueSymbol)`

Two of these methods, getNeighborhood(int, Generation) and evolve(boolean[]), were in Project 1 and contained code that was specific to elementary CAs.
This code has been moved to a new class, ElementaryRule, that inherits from Rule.
A second Rule subclass, TotalisticRule, has also been created that simulates a different type of 1D, two-state CA.

To make Automaton compatible with both ElementaryRule and TotalisticRule, the class now uses createRule(int) to instantiate Rule objects, rather than calling a Rule subclass constructor with the `new` keyword.
Each subclass of Automaton overrides createRule(int) to make it return an object of the corresponding Rule subclass: ElementaryRule for ElementaryAutomaton and TotalisticRule for TotalisticAutomaton.

Additional information about the classes is given in the following sections.

## Generation

This class is unchanged from Project 1.
It represents the cells of a 1D, two-state CA at a single moment in time.

## Rule

The abstract Rule class represents any rule that governs the evolution of a 1D, two-state CA.<sup id="a1">[1](#f1)</sup>
The behavior of different rule types (e.g., elementary, totalistic) is simulated by extending the class and overriding two methods:

* `getNeighborhood(int idx, Generation gen)`: Return the cell states in the neighborhood of the cell with the given index.

* `evolve(boolean[] neighborhood)`: Return the next state of a cell in a neighborhood with the given states.

Subclasses of Rule must also override a method that returns a string representation of a Rule object:

* `getRuleTable(char falseSymbol, char trueSymbol)`: Return the table that depicts the rule using the given characters to represent false and true.

The remaining methods were part of Rule in Project 1 and work the same way in this assignment.

After you write ElementaryRule and TotalisticRule, you may find that the classes contain similar helper methods.
To remove the [duplicate code](https://en.wikipedia.org/wiki/Duplicate_code), move these methods into Rule and change their access modifiers to `protected`.

## RuleNumException

The Rule constructor in Project 1 handled invalid rule numbers by replacing them with the closest valid number.
In this project, we will throw a custom exception instead.

In each subclass of Rule, have the constructor check whether the given integer is a valid number for the corresponding rule type.
If it is outside the range, throw a RuleNumException.

* `RuleNumException(int min, int max)`: Initialize the object so that the inherited method [getMessage](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Exception.html#%3Cinit%3E(java.lang.String)) returns

  ```java
  "ruleNum is outside the range [min, max]."
  ```

  where `"min"` and `"max"` are replaced by the parameter values.

## ElementaryRule

ElementaryRule represents any one of the 256 rules that govern the evolution of elementary CAs.
The class behaves just like Rule from Project 1, but it has one extra method:

* `getRuleTable(char falseSymbol, char trueSymbol)`: Return a two-line representation of the elementary rule table.
The first line shows the 8 possible neighborhoods separated by spaces; the second shows the states of the center cells in the next generation.
Align each state character on the second line with the center of the corresponding neighborhood.
For example, below is the output of the method for rule 22 when given the arguments `'0'` and `'1'`:

  ```java
  "111 110 101 100 011 010 001 000" + System.lineSeparator() +
  " 0   0   0   1   0   1   1   0 "
  ```

## TotalisticRule

TotalisticRule represents any one of the 64 rules that govern the evolution of 1D, two-state [totalistic CAs](https://mathworld.wolfram.com/TotalisticCellularAutomaton.html) with a neighborhood radius of 2.

Like elementary rules, totalistic rules determine the next state of a cell by looking at its current state and the states of its neighbors.
Unlike elementary rules, totalistic rules do not care about the arrangement of states in the neighborhood, only the *total* number true states.
Totalistic rules map each total to the state of the center cell in the next generation.

To make these rules a bit more interesting, we will use a neighborhood radius of 2.<sup id="a2">[2](#f2)</sup>
This means that the neighborhood of a cell consists of five cells: the two nearest neighbors on the left, the cell itself, and the two nearest neighbors on the right.
Since the states of these cells can each be true or false, the total number of true states (i.e., the neighborhood total) is an integer between 0 and 5.

Totalistic rules can be depicted graphically with tables that are similar to those for elementary rules.
For example, here is the table for totalistic rule 22:
|  5  |  4  |  3  |  2  |  1  |  0  |
|:---:|:---:|:---:|:---:|:---:|:---:|
|false|true |false|true |true |false|

The top row shows the possible neighborhood totals in descending order.
The bottom row shows the states of cells with the corresponding neighborhood totals in the next generation.
For instance, if a cell has a neighborhood with 3 true states, totalistic rule 22 says that the cell's next state is false.

Totalistic rules are numbered analogously to elementary rules: replace false and true with 0 and 1 in the bottom row of the table and interpret the result as a binary number.
For the table above, the binary number is 010110, which is equal to 22 in base 10 (0 + 16 + 0 + 4 + 2 + 0).

Below are descriptions of the methods that TotalisticRule overrides in Rule:

* `getNeighborhood(int idx, Generation gen)`: This method works just like its ElementaryRule counterpart, but it returns a boolean array with five (rather than three) elements using this format:

  ```java
  [cellStates[idx-2], cellStates[idx-1], cellStates[idx], cellStates[idx+1], cellStates[idx+2]]
  ```

  If the index corresponds to the first or last two cells, use circular boundary conditions to get the states of the neighbors.
  A bit of modular arithmetic will simplify this calculation, but don't use the `%` operator because the result can be negative.
  Instead, use the [floorMod method](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Math.html#floorMod(int,int)) from the [Math class](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Math.html).

* `evolve(boolean[] neighborhood)`: Return the next state of a cell in a neighborhood with the given states by applying the totalistic rule.

* `getRuleTable(char falseSymbol, char trueSymbol)`: Return a two-line representation of the totalistic rule table.
Below is the output for rule 22 when given the arguments `'0'` and `'1'`:
  
  ```java
  "5 4 3 2 1 0" + System.lineSeparator() +
  "0 1 0 1 1 0"
  ```

## Automaton

The abstract Automaton class represents any 1D, two-state CA that evolves according to a rule represented by the Rule class.
Automaton behaves just like it did in Project 1, but it has two extra methods:

* `createRule(int ruleNum)`: Each subclass of Automaton overrides this method so that it creates and returns an object of a particular Rule subclass.
Use the method to initialize the rule field in each constructor, rather than using the `new` keyword.

* `getRuleTable()`: Return a string representation of the table that depicts the rule governing the automaton.
Use the characters assigned to the fields falseSymbol and trueSymbol to represent the cell states.

## ElementaryAutomaton & TotalisticAutomaton

ElementaryAutomaton and TotalisticAutomaton represent any 1D, two-state CAs that evolve according to the rules represented by ElementaryRule and TotalisticAutomaton, respectively.
Each subclass overrides createRule(int) so that it instantiates and returns an object of the corresponding Rule subclass with the given rule number.
The ElementaryAutomaton and TotalisticAutomaton constructors simply pass their arguments to the parent constructor with the matching parameter list.

## Design Patterns and Extensibility

The changes made to Rule and Automaton make these classes far more [extensible](https://en.wikipedia.org/wiki/Extensibility) than the versions in Project 1.
In the future, we can easily create new types of 1D, two-state CAs [without modifying or recompiling](https://en.wikipedia.org/wiki/Open%E2%80%93closed_principle) any of our Project 2 classes.
All we have to do is follow these steps:

1. Write a new subclass of Rule that simulates the CA rule of interest.
2. Write a new subclass of Automaton that has createRule(int) instantiate and return an object of the new Rule subclass.

This degree of extensibility is accomplished by following two [design patterns](https://en.wikipedia.org/wiki/Software_design_pattern): the [strategy pattern](https://en.wikipedia.org/wiki/Strategy_pattern) and the [factory method pattern](https://en.wikipedia.org/wiki/Factory_method_pattern).

The strategy pattern is followed by encapsulating the code that implements each rule type in a separate Rule subclass and having Automaton interact with whichever Rule it uses through a reference of the parent type.
By doing this, Automaton has no idea whether the Rule object is an ElementaryRule or a TotalisticRule.
Automaton just calls getNeighborhood(int, Generation) and evolve(boolean[]) on the reference, and the correct versions of the methods are executed [polymorphically](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html).

The factory method pattern is followed by initializing the rule field in each Automaton constructor with the abstract method createRule(int).
Doing this allows the subclasses of Automaton to choose the subclass of the Rule object by overriding the factory method.
The result is that the Automaton constructors can initialize the rule field without knowing the subclass of the Rule objects they create.<sup id="a3">[3](#f3)</sup>

## Footnotes

<a id="f1">[1.](#a1)</a> Specifically, the class represents rules with these properties:

1. The cell states of the next generation are determined entirely by the states of the current generation (cf. [second-order CA](https://en.wikipedia.org/wiki/Second-order_cellular_automaton)).
2. The cell states are updated synchronously (cf. [asynchronous CA](https://en.wikipedia.org/wiki/Asynchronous_cellular_automaton)).

<a id="f2">[2.](#a2)</a> The totalistic rules with a neighborhood radius of 1 are a subset of the elementary rules.
For instance, totalistic rule 2 is equivalent to elementary rule 22.
(Note that all the columns of the rule 22 table with a true state on the bottom row have a neighborhood with exactly one true state.)

<a id="f3">[3.](#a3)</a> The Nim class from Lab 6 used [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) for a similar purpose: to initialize its Player fields without knowing the subclasses of the Player objects.
Dependency injection can't be used here because Automaton has a constructor that instantiates a Rule with a number it reads from a text file.
To use dependency injection, the Rule object must be created before calling the Automaton constructor, and this is impossible if the rule number is unknown.
