# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR21 = QuantumCircuit(q0)

mutantQGR21.h(q0)
mutantQGR21.id(q0)

