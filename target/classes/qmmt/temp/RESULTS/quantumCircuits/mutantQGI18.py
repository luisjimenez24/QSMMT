# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI18 = QuantumCircuit(q0)

mutantQGI18.h(q0)
mutantQGI18.z(q0)
mutantQGI18.sdg(q0)

