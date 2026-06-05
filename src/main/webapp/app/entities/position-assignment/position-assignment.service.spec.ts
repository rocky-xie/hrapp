import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PositionAssignment } from '@/shared/model/position-assignment.model';

import PositionAssignmentService from './position-assignment.service';

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
  describe('PositionAssignment Service', () => {
    let service: PositionAssignmentService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PositionAssignmentService();
      currentDate = new Date();
      elemDefault = new PositionAssignment(123, false, currentDate, currentDate, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          endDate: dayjs(currentDate).format(DATE_FORMAT),
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

      it('should create a PositionAssignment', async () => {
        const returnedFromService = {
          id: 123,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          endDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { startDate: currentDate, endDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PositionAssignment', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PositionAssignment', async () => {
        const returnedFromService = {
          primaryOwner: true,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          endDate: dayjs(currentDate).format(DATE_FORMAT),
          responsibilityScope: 'BBBBBB',
          active: true,
          ...elemDefault,
        };

        const expected = { startDate: currentDate, endDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PositionAssignment', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PositionAssignment', async () => {
        const patchObject = { responsibilityScope: 'BBBBBB', ...new PositionAssignment() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDate: currentDate, endDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PositionAssignment', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PositionAssignment', async () => {
        const returnedFromService = {
          primaryOwner: true,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          endDate: dayjs(currentDate).format(DATE_FORMAT),
          responsibilityScope: 'BBBBBB',
          active: true,
          ...elemDefault,
        };
        const expected = { startDate: currentDate, endDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PositionAssignment', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PositionAssignment', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PositionAssignment', async () => {
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
