# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI0 = QuantumCircuit(q0)

mutantQGI0.h(q0)
mutantQGI0.id(q0)
mutantQGI0.z(q0)

