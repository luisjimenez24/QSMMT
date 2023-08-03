# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR18 = QuantumCircuit(q0)

mutantQGR18.h(q0)
mutantQGR18.sdg(q0)

