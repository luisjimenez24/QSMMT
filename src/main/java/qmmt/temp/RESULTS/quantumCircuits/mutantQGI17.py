# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI17 = QuantumCircuit(q0)

mutantQGI17.h(q0)
mutantQGI17.z(q0)
mutantQGI17.s(q0)

