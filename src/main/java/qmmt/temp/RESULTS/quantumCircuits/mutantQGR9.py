# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR9 = QuantumCircuit(q0)

mutantQGR9.z(q0)
mutantQGR9.z(q0)

