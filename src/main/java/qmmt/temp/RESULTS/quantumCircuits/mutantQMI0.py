# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQMI0 = QuantumCircuit(q0)

mutantQMI0.h(q0)
mutantQMI0.m(q0)
mutantQMI0.z(q0)

