# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR7 = QuantumCircuit(q0)

mutantQGR7.s(q0)
mutantQGR7.z(q0)

