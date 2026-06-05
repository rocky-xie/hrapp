import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { TrainingGoal } from '@/shared/model/training-goal.model';

import TrainingGoalService from './training-goal.service';

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
  describe('TrainingGoal Service', () => {
    let service: TrainingGoalService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TrainingGoalService();
      currentDate = new Date();
      elemDefault = new TrainingGoal(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 'DRAFT');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
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

      it('should create a TrainingGoal', async () => {
        const returnedFromService = {
          id: 123,
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { startDate: currentDate, targetDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a TrainingGoal', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a TrainingGoal', async () => {
        const returnedFromService = {
          goalName: 'BBBBBB',
          goalDescription: 'BBBBBB',
          targetLevelDescription: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          status: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDate: currentDate, targetDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a TrainingGoal', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a TrainingGoal', async () => {
        const patchObject = {
          goalDescription: 'BBBBBB',
          targetLevelDescription: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          ...new TrainingGoal(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDate: currentDate, targetDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a TrainingGoal', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of TrainingGoal', async () => {
        const returnedFromService = {
          goalName: 'BBBBBB',
          goalDescription: 'BBBBBB',
          targetLevelDescription: 'BBBBBB',
          startDate: dayjs(currentDate).format(DATE_FORMAT),
          targetDate: dayjs(currentDate).format(DATE_FORMAT),
          status: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDate: currentDate, targetDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of TrainingGoal', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a TrainingGoal', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a TrainingGoal', async () => {
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
