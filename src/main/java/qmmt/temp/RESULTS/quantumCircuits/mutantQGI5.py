# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI5 = QuantumCircuit(q0)

mutantQGI5.h(q0)
mutantQGI5.id(q0)
mutantQGI5.z(q0)

