import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { DispatchAssignment } from '@/shared/model/dispatch-assignment.model';

import DispatchAssignmentService from './dispatch-assignment.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('DispatchAssignment Service', () => {
    let service: DispatchAssignmentService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new DispatchAssignmentService();
      currentDate = new Date();
      elemDefault = new DispatchAssignment(123, 0, 0, 0, 'AAAAAAA', 0, currentDate, currentDate, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          expectedEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a DispatchAssignment', async () => {
        const returnedFromService = {
          id: 123,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          expectedEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { startDate: currentDate, expectedEndDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a DispatchAssignment', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a DispatchAssignment', async () => {
        const returnedFromService = {
          candidateId: 1,
          personId: 1,
          partnerOrganizationId: 1,
          department: 'BBBBBB',
          positionId: 1,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          expectedEndDate: dayjs(currentDate).format(DATE_FORMAT),
          observationDays: 1,
          status: 'BBBBBB',
          finalDecision: 'BBBBBB',
          note: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDate: currentDate, expectedEndDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a DispatchAssignment', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a DispatchAssignment', async () => {
        const patchObject = { candidateId: 1, personId: 1, observationDays: 1, finalDecision: 'BBBBBB', ...new DispatchAssignment() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDate: currentDate, expectedEndDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a DispatchAssignment', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of DispatchAssignment', async () => {
        const returnedFromService = {
          candidateId: 1,
          personId: 1,
          partnerOrganizationId: 1,
          department: 'BBBBBB',
          positionId: 1,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          expectedEndDate: dayjs(currentDate).format(DATE_FORMAT),
          observationDays: 1,
          status: 'BBBBBB',
          finalDecision: 'BBBBBB',
          note: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDate: currentDate, expectedEndDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of DispatchAssignment', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a DispatchAssignment', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a DispatchAssignment', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
