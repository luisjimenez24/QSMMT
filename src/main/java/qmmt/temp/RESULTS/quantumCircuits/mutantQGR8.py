# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR8 = QuantumCircuit(q0)

mutantQGR8.y(q0)
mutantQGR8.z(q0)

