# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI3 = QuantumCircuit(q0)

mutantQGI3.h(q0)
mutantQGI3.x(q0)
mutantQGI3.z(q0)

