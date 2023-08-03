# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR20 = QuantumCircuit(q0)

mutantQGR20.h(q0)
mutantQGR20.z(q0)

