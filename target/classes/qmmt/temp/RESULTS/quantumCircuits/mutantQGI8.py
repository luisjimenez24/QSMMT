# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI8 = QuantumCircuit(q0)

mutantQGI8.h(q0)
mutantQGI8.y(q0)
mutantQGI8.z(q0)

