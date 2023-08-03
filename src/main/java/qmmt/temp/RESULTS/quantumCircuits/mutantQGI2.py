# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI2 = QuantumCircuit(q0)

mutantQGI2.h(q0)
mutantQGI2.tdg(q0)
mutantQGI2.z(q0)

