# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR2 = QuantumCircuit(q0)

mutantQGR2.tdg(q0)
mutantQGR2.z(q0)

