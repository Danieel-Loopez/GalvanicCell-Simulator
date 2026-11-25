Galvanic Corrosion Calculator

Java-based electrochemical analysis tool for galvanic cells.

Overview

Galvanic Corrosion Calculator is a command-line application developed in Java to analyze electrochemical behavior between different metals and calculate the electrical potential of galvanic cells.

The application integrates electrochemical principles with computational logic to evaluate electrode potentials, identify oxidation and reduction processes, and generate balanced electrochemical equations.

Core Features

Metal Database: Contains standard reduction potentials and electrochemical information for 17 metals and chemical species.

Electrode Potential Calculation: Implements the Nernst equation to calculate electrode potentials under non-standard concentration conditions.

Galvanic Cell Analysis: Determines the anode, cathode, and resulting cell potential.

Electrochemical Equation Balancing: Calculates electron equivalence and balances oxidation and reduction half-reactions.

Input Validation: Implements input handling and validation to prevent invalid user entries.

Interactive CLI: Provides a menu-driven interface for accessing the application's functionality.

Programming Language:

Java 

Development Environment:

IntelliJ IDEA

User Interface

Command-Line Interface

Mathematical Model

Nernst Equation

Electrochemical Model

The application implements the Nernst equation to calculate electrode potential under non-standard conditions:
E = E° + 0.0592 / n * log10(C)

Where:

(E): Electrode potential.

(E°): Standard reduction potential.

(n): Number of transferred electrons.

(C): Concentration.

Open the project using IntelliJ IDEA.

Load the Maven configuration.

Navigate to CalculadoraDeCorrosion.java.

Run the main method.

Development Team:

Martinez Dominguez Nery Alexander

Daniel López Ruiz

Aldair Sánchez Salinas

Project Scope

This project demonstrates the application of Java programming, mathematical modeling, electrochemical principles, algorithmic problem-solving, and input validation in the development of a command-line software tool.
