# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR10 = QuantumCircuit(q0)

mutantQGR10.sx(q0)
mutantQGR10.z(q0)

