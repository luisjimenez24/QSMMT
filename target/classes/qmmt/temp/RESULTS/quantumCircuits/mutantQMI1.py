# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQMI1 = QuantumCircuit(q0)

mutantQMI1.h(q0)
mutantQMI1.z(q0)
mutantQMI1.m(q0)

