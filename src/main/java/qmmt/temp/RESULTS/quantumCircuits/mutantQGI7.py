# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI7 = QuantumCircuit(q0)

mutantQGI7.h(q0)
mutantQGI7.s(q0)
mutantQGI7.z(q0)

