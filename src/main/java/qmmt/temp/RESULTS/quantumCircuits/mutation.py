# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutation = QuantumCircuit(q0)

mutation.z(q0)
mutation.z(q0)

