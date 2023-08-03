# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR5 = QuantumCircuit(q0)

mutantQGR5.id(q0)
mutantQGR5.z(q0)

