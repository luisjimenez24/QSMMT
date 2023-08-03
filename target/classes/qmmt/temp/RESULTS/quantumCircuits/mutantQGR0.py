# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR0 = QuantumCircuit(q0)

mutantQGR0.id(q0)
mutantQGR0.z(q0)

