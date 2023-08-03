# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR15 = QuantumCircuit(q0)

mutantQGR15.h(q0)
mutantQGR15.id(q0)

