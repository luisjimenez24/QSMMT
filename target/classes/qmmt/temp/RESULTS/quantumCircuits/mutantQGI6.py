# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI6 = QuantumCircuit(q0)

mutantQGI6.h(q0)
mutantQGI6.sdg(q0)
mutantQGI6.z(q0)

