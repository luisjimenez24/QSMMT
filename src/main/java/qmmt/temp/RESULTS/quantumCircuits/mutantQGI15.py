# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI15 = QuantumCircuit(q0)

mutantQGI15.h(q0)
mutantQGI15.z(q0)
mutantQGI15.id(q0)

