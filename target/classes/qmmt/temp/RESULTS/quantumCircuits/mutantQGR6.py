# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR6 = QuantumCircuit(q0)

mutantQGR6.sdg(q0)
mutantQGR6.z(q0)

