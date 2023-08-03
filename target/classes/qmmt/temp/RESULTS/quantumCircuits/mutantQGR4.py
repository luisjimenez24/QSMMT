# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR4 = QuantumCircuit(q0)

mutantQGR4.h(q0)
mutantQGR4.z(q0)

