# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR3 = QuantumCircuit(q0)

mutantQGR3.x(q0)
mutantQGR3.z(q0)

