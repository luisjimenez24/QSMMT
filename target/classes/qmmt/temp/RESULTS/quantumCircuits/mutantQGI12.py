# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI12 = QuantumCircuit(q0)

mutantQGI12.h(q0)
mutantQGI12.z(q0)
mutantQGI12.tdg(q0)

