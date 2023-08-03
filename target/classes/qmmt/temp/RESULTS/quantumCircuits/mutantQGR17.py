# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR17 = QuantumCircuit(q0)

mutantQGR17.h(q0)
mutantQGR17.s(q0)

